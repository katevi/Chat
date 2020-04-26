import { Component, OnInit } from '@angular/core';
import { WebsocketService } from '../services/websocket.service';
import { Message } from '../models/message.model';
import { User } from '../models/user.model';

@Component({
  selector: 'home-root',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  title = 'Chat';

  public currentMsg: String = "";
  public messages: Message[] = [];
  public hasAuthed: boolean = false;
  public currentReceiver: string = "";

  // -- User Login info
  public inputFullName: string = "";
  public inputUsername: string = "";
  public inputPassword: string = "";
  public inputGitHubAccount: string = "";

  constructor(
    public websocketService: WebsocketService
  ) { }

  ngOnInit(): void {
    this.init();
  }

  /**
   * Indicates whether login attempt should be allowed.
   */
  public isLoginPossible(): boolean {
    return (
      (this.inputFullName != "") &&
      (this.inputUsername != "") &&
      (this.inputPassword != "") &&
      (this.inputGitHubAccount != "")
    );
  }

  /**
   * Handles login button click.
   */
  public onLoginBtnClicked() {
    if (!this.isLoginPossible()) {
      return;
    }
    let user = new User(this.inputFullName,
      this.inputUsername,
      this.inputPassword,
      this.inputGitHubAccount);
    this.hasAuthed = this.isLoginPossible();
    this.websocketService.registerUser(user);
  }

  /**
   * Handles 'Send' button press.
   * @param msg message to be sent.
   */
  public onSendMsgBtnClicked(msgContent: string) {
    if (msgContent == "") {
      console.error("empty msg!")
      return;
    }
    let msg: Message;
    if (this.currentReceiver == "") {
        msg = new Message(this.inputFullName, null, msgContent);
    } else {
        msg = new Message(this.inputFullName, this.currentReceiver, msgContent);
    }
    this.websocketService.sendMsg(msg);
    this.currentMsg = "";
    this.currentReceiver = "";
  }

  /**
 * Perform component initialization.
 */
  private init(): void {
    this.websocketService.connect();
    // -- Define logic for new message receive
    this.websocketService.getLastReceivedMsg().subscribe(
      (newMsg: Message) => {
        // -- the function is triggered every time ...
        // -- ... new message is received vai STOMP.
        if (newMsg == null) {
          return;
        }
        this.messages.push(newMsg)
      }
    )
  }
}
