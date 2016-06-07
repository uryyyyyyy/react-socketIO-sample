import React from "react";
import Sound from "./Sound";
import FlatButton from "material-ui/FlatButton";

export default class Home extends React.Component {

  emitSocket(props){
    console.log("normal button clicked");
    props.socket.emit('normal request')
  }

  emitSocket2(props){
    console.log("broadcast button clicked");
    props.socket.emit('broadcast request')
  }

  sound() {
    const sound = new Sound('sound-file');
    sound.play()
  }

  render() {
    return (
      <div>
        <FlatButton label="Emit" primary={true} onMouseDown={() => this.emitSocket(this.props)} />
        <FlatButton label="Emit BroadCast" primary={true} onMouseDown={() => this.emitSocket2(this.props)} />
        <FlatButton label="Sound" primary={true} onMouseDown={() => this.sound()} />
      </div>
    )
  }
}