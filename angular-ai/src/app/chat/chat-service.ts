import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { ChatRespose } from './chat-respose';

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  private readonly API = '/api/chat';

  private http = inject(HttpClient);

  sendChatMessage(message: string) {
    return this.http.post<ChatRespose>(this.API, { message })
  }
}
