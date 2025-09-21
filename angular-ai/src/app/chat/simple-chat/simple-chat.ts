import { ChatService } from './../chat-service';
import { Component, ElementRef, inject, signal, ViewChild } from '@angular/core';
import {MatCardModule} from '@angular/material/card';
import { MatToolbar } from "@angular/material/toolbar";
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { NgClass } from "@angular/common";
import { catchError, throwError } from 'rxjs';

@Component({
  selector: 'app-simple-chat',
  imports: [MatCardModule, MatToolbar, MatInputModule, MatButtonModule, MatIconModule, FormsModule, NgClass],
  templateUrl: './simple-chat.html',
  styleUrl: './simple-chat.scss'
})
export class SimpleChat {

  @ViewChild('chatHistory')
  private chatHistory!: ElementRef;

  private ChatService = inject(ChatService);

  userInput = '';
  isLoading = false;

  local = false;

  messages = signal([
    { text: 'Hello, how can I help you today?', isBot: true}
  ]);

  sendMessage(){
    this.trimUserMessage();
    if(this.userInput != '' && !this.isLoading){
      this.updateMessages(this.userInput);
      this.isLoading = true;
      if (this.local){
        this.simulateResponse();
      } else {
        this.sendChatMessage();
      }
    }
  }

  private sendChatMessage(){
    this.ChatService.sendChatMessage(this.userInput)
    .pipe(
      catchError(() => {
        this.updateMessages('Sorry, there was an error processing your request.', true);
        this.isLoading = false;
        return throwError(() => new Error ('Error occurred while sending chat message.'));
      })
    )
    .subscribe(response => {
      this.updateMessages(response.message, true);
      this.userInput = '';
      this.isLoading = false;
    });
  }

  private updateMessages(text: string, isBot = false){
    this.messages.update(messages => [...messages, { text, isBot}]);
    this.scrollToBottom();
  }

  private trimUserMessage(){
    this.userInput = this.userInput.trim();
  }

  private simulateResponse(){
    setTimeout(() => {
      const response = 'This is a simulated response from Chat AI.';
      this.updateMessages(response, true);
      this.userInput = '';
      this.isLoading = false;
    }, 2000);
  }

  private scrollToBottom(){
    try {
      this.chatHistory.nativeElement.scrollTop = this.chatHistory.nativeElement.scrollHeight;
    } catch(err) { }
  }
}
