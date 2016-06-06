import React from 'react'

export default class Home extends React.Component {


  componentDidMount() {
    this.props.socket.on('normal response', function (data) {
      console.log("normal response");
      console.log(data)
    });

    this.props.socket.on('broadcast response', function (data) {
      console.log("broadcast response");
      console.log(data)
    });
  }

  render() {
    // var dom = this.props.actions.map(action => <span key={todo.id}>{action.name}</span>);
    return (
      <div>
      </div>
    )
  }
}