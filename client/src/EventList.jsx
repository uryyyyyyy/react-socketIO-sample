import React from "react";

export default class Home extends React.Component {

  constructor() {
    super();
    this.state = { text: 'default' }
  }

  // socket.onmessage = function(e) {
  //   console.log("receive message: " + e.data);
  // };


  componentDidMount() {
    this.props.socket.onmessage = (data => {
      window.alert(data.data)
      this.setState({text: "changed"})
    });
  }

  render() {
    // var dom = this.props.actions.map(action => <span key={todo.id}>{action.name}</span>);
    return (
      <div>
        {this.state.text}
      </div>
    )
  }
}