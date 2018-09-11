import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { WEB_SOCKETS_URL} from '../utils/Constants';
import { Tag as TagD } from 'antd';
import { Button, FormControl, FormGroup, InputGroup, Table, Grid, Row, Col} from 'react-bootstrap';
import SockJsClient from 'react-stomp'
var tags = " ";
class Tag extends Component {
    constructor(props) {
        super(props);
        this.state = {
            tags: [],
            value: " " ,
        };
        this.handleTag = this.handleTag.bind(this);
    }
    
    handleTag = (e) => {
        const form = e.target;
        var input = form["tag"].value;

        this.sendMessage(input);
        tags += "<br>" + input;
        form.reset();
        this.setState({
            tags: [...this.state.tags, input],
        })
        e.preventDefault();
    }

    sendMessage = (msg) => {
        try {
            this.clientRef.sendMessage('/ldod-game/tags', JSON.stringify({ urlId: this.props.id, authorId: localStorage.getItem("currentUser"), msg: msg, vote: 1}));
            return true;

        } catch(e) {
            return false;
        }
    }

    handleChange(event) {
        const value = event.target.value;
        
        this.setState({
          value: value
        });
    }

    render() {
        const tagViews = [];
        let messages = this.state.tags;
        messages.forEach((m, mIndex) => {
            tagViews.push(<TagD color="#e1b12c" key={mIndex} >{m}</TagD>)
        });
        return (
            <div> 
                <SockJsClient
                    url={WEB_SOCKETS_URL}
                    topics={['/topic/tags']}
                    ref={ (client) => { this.clientRef = client }}
                    onMessage={(message) => this.props.handleMessageTag(message)} />
                <Grid>
                    <Row>
                        <Col md={4} mdOffset={4} xs={5}>
                        <form id="form" autoComplete="off" onSubmit={(e) => {this.handleTag(e)}}>
                        <FormGroup validationState={ this.props.disabled === true ? "warning" : null}>
                            <InputGroup>
                                <FormControl 
                                    placeholder="Tag this paragraph" 
                                    id="tag"
                                    type="text"
                                    spellCheck="true"
                                    onChange={this.handleChange.bind(this)} 
                                    autoFocus />
                                </InputGroup>
                                <FormControl.Feedback />
                                <Button className="btn btn-primary" disabled={this.props.disabled} type="submit">
                                    <span className="glyphicon glyphicon-plus"></span>
                                </Button>
                        </FormGroup>
                        </form>
                        </Col>    
                    </Row>
                <Table>
                    <thead>
                        <tr>
                            <th><span className="glyphicon glyphicon-tag"></span></th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>
                                <div>{tagViews}</div>			
                            </td>
                        </tr>
                    </tbody>
            	</Table>
                </Grid>
            </div>
        );
    }
}


export default withRouter(Tag);