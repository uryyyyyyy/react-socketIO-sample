import React from 'react'
import ReactDOM from 'react-dom'
import EmitButton from './src/EmitButton'
import EventList from './src/EventList'

import io from 'socket.io-client'
let socket = io(`http://localhost:3000`);

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

ReactDOM.render(<Route />, document.getElementById('root'));
