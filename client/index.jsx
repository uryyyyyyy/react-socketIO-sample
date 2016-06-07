import React from "react";
import ReactDOM from "react-dom";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import EmitButton from "./src/EmitButton";
import EventList from "./src/EventList";
import io from "socket.io-client";
let socket = io();

export default class Route extends React.Component {

  render() {
    return (
      <div>
        <EmitButton socket={socket} />
        <EventList socket={socket} />
      </div>
    )
  }
}

ReactDOM.render(
  <MuiThemeProvider muiTheme={getMuiTheme()} >
    <Route />
  </MuiThemeProvider>,
  document.getElementById('root'));

// var s = new Sound();
// s.play();
