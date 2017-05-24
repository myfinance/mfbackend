import React from 'react';
import {loadFromServer} from "./backend";

export default class InstrumentSearch extends React.Component {
    render() {
        const body = this.state.instruments.map(instrument => <tr key={instrument.isin}><td>{instrument.isin}</td><td>{instrument.desc}</td></tr>);
        return (
            <div>
                <input ref={input => this.input = input}
                       onChange={event => this.updateModel(event)}
                       value={this.state.isin} />
                <p>{this.state.isin}, World</p>
                <button
                    onClick={() => this.reset()}>
                    Clear
                </button>
                <button
                    onClick={() => this.search()}>
                    Search
                </button>
                <table>
                    <thead>
                    <tr><th>isin</th><th>desc</th></tr>
                    </thead>
                    <tbody>
                    {body}
                    </tbody>
                </table>
            </div>);
    }
    constructor(props) {
        super(props);
        this.state = {
            isin: this.props.isin,
            instruments: []
        };
    }
    reset() {
        this.setState({isin: ""});
        this.input.focus();
    }

    search() {
        loadFromServer(
            instruments => this.setState({instruments}),
            err => console.error('LOADING GREETINGS FAILED:', err)
        );
    }

    updateModel(event) {
        this.setState({isin: event.target.value});
    }
}