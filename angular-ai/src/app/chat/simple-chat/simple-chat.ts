import { ChatService } from './../chat-service';
import { Component, ElementRef, inject, signal, ViewChild } from '@angular/core';
import { NgClass } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { catchError, throwError } from 'rxjs';

@Component({
  selector: 'app-simple-chat',
  imports: [MatCardModule, MatToolbarModule, MatInputModule, MatButtonModule, MatIconModule, FormsModule, NgClass],
  templateUrl: './simple-chat.html',
  styleUrl: './simple-chat.scss'
})
export class SimpleChat {

  @ViewChild('chatHistory')
  private chatHistory!: ElementRef; // Reference to the chat history element for scrolling


  private ChatService = inject(ChatService);

  userInput = '';
  isLoading = false;

  local = false;

  messages = signal([
    { text: 'Hello, how can I help you today?', isBot: true }
  ]);

  sendMessage() {
    this.trimUserMessage();
    if (this.userInput !== '' && !this.isLoading) {
      this.upudateMessages(this.userInput);
      this.isLoading = true;
      if (this.local) {
        this.simulateResponse();
      } else {
          this.sendChatMessage();
      }
    }
  }

  private sendChatMessage() {
    this.ChatService.sendChatMessage(this.userInput)
    .pipe(
      catchError(() => {
        this.upudateMessages('An error occurred while sending the message. Please try again.', true);
        this.isLoading = false;
        return throwError(() => new Error('Error sending message'));
      })
    )
    .subscribe(response => {
      this.upudateMessages(response.message, true);
      this.userInput = '';
      this.isLoading = false;
    });
  }

  private upudateMessages(text:string, isBot = false) {
    this.messages.update(messages => [...messages, { text: text, isBot: isBot }]);
    this.scrollToBottom();
  }

  private trimUserMessage() {
    this.userInput = this.userInput.trim();
  }

  private simulateResponse() {
    setTimeout(() => {
      const response = 'This is a simulated response from Chat AI';
      this.upudateMessages(response, true);
      this.userInput = '';
      this.isLoading = false;
    }, 2000);
  }

  private scrollToBottom() {
    try {
      this.chatHistory.nativeElement.scrollTop = this.chatHistory.nativeElement.scrollHeight;
    } catch (err) {}
  }
}
