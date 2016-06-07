import React from 'react'

export default class Home extends React.Component {

  constructor() {
    super();
    this.state = { text: 'default' }
  }

  componentDidMount() {
    this.props.socket.on('normal response', (data) => {
      window.alert(data.name)
    });

    this.props.socket.on('broadcast response', (data) => {
      window.alert(data.name);
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