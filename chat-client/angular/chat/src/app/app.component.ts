import { Component } from '@angular/core';
import { WebsocketService } from './services/websocket.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Chat';

  public currentMsg: String = "";

  constructor(public websocketService: WebsocketService) {
    
  }
  

  public onSendMsgBtnClicked(msg) { 
    console.log("send msg button has been clicked" + msg)
  }
}
