import React from "react";
import Sound from './Sound'

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
        <button onClick={() => this.emitSocket(this.props)}>Emit</button>
        <button onClick={() => this.emitSocket2(this.props)}>Emit BroadCase</button>
        <button onClick={() => this.sound()}>Sound</button>
      </div>
    )
  }
}