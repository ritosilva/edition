import React, {Component} from "react";
import "./ActivityMenu.css";
import {connect} from "react-redux";
import {DropdownButton, MenuItem, ButtonToolbar} from "react-bootstrap";
import NetworkGraphContainer from "../containers/NetworkGraphContainer";
import {setCurrentVisualization, setPotentialVisualizationTechnique, setPotentialSemanticCriteria, setSemanticCriteriaDataLoaded} from "../actions/index";
import SquareGrid from "../components/SquareGrid";

import {
  VIS_SQUARE_GRID,
  VIS_NETWORK_GRAPH,
  VIS_WORD_CLOUD,
  BY_SQUAREGRID_EDITIONORDER,
  CRIT_EDITION_ORDER,
  CRIT_CHRONOLOGICAL_ORDER,
  CRIT_TEXT_SIMILARITY,
  CRIT_HETERONYM,
  CRIT_TAXONOMY,
  CRIT_WORD_RELEVANCE
} from "../constants/history-transitions";

const mapDispatchToProps = dispatch => {
  return {
    setCurrentVisualization: currentVisualization => dispatch(setCurrentVisualization(currentVisualization)),
    setPotentialVisualizationTechnique: potentialVisualizationTechnique => dispatch(setPotentialVisualizationTechnique(potentialVisualizationTechnique)),
    setPotentialSemanticCriteria: potentialSemanticCriteria => dispatch(setPotentialSemanticCriteria(potentialSemanticCriteria)),
    setSemanticCriteriaDataLoaded: semanticCriteriaDataLoaded => dispatch(setSemanticCriteriaDataLoaded(semanticCriteriaDataLoaded))
  };
};

const mapStateToProps = state => {
  return {
    fragments: state.fragments,
    fragmentIndex: state.fragmentIndex,
    currentVisualization: state.currentVisualization,
    recommendationArray: state.recommendationArray,
    recommendationIndex: state.recommendationIndex,
    recommendationLoaded: state.recommendationLoaded
  };
};

class ConnectedActivityMenu extends Component {
  constructor(props) {
    super(props);

    this.activityToRender = (<div></div>);
    this.state = {
      show: true
    };

    this.toggleActivityNetworkGraphTextSimilarity = this.toggleActivityNetworkGraphTextSimilarity.bind(this);

    this.toggleActivityNetworkGraphHeteronym = this.toggleActivityNetworkGraphHeteronym.bind(this);

    this.toggleActivityNetworkGraphDate = this.toggleActivityNetworkGraphDate.bind(this);

    this.toggleActivityNetworkGraphTaxonomy = this.toggleActivityNetworkGraphTaxonomy.bind(this);
  }

  toggleActivityNetworkGraphTextSimilarity() {
    this.props.setPotentialVisualizationTechnique(VIS_NETWORK_GRAPH);
    this.props.setPotentialSemanticCriteria(CRIT_TEXT_SIMILARITY);
    this.activityToRender = (<NetworkGraphContainer onChange={this.props.onChange}/>);
    this.setState(prevState => ({
      show: !prevState.show
    }));
  }

  toggleActivityNetworkGraphHeteronym() {

    this.props.setPotentialVisualizationTechnique(VIS_NETWORK_GRAPH);
    this.props.setPotentialSemanticCriteria(CRIT_HETERONYM);
    this.activityToRender = (<NetworkGraphContainer onChange={this.props.onChange}/>);
    this.setState(prevState => ({
      show: !prevState.show
    }));
  }

  toggleActivityNetworkGraphDate() {

    this.props.setPotentialVisualizationTechnique(VIS_NETWORK_GRAPH);
    this.props.setPotentialSemanticCriteria(CRIT_CHRONOLOGICAL_ORDER);
    this.activityToRender = (<NetworkGraphContainer onChange={this.props.onChange}/>);
    this.setState(prevState => ({
      show: !prevState.show
    }));
  }

  toggleActivityNetworkGraphTaxonomy() {

    this.props.setPotentialVisualizationTechnique(VIS_NETWORK_GRAPH);
    this.props.setPotentialSemanticCriteria(CRIT_TAXONOMY);
    this.activityToRender = (<NetworkGraphContainer onChange={this.props.onChange}/>);
    this.setState(prevState => ({
      show: !prevState.show
    }));
  }

  render() {

    if (this.state.show) {

      this.activityToRender = (<ButtonToolbar>
        <DropdownButton id="1" bsSize="large" bsStyle="primary" title="Quero ler fragmentos semelhantes a este por...">
          <MenuItem eventKey="1" onClick={this.toggleActivityNetworkGraphTextSimilarity}>
            Semelhança de texto
          </MenuItem>
          <MenuItem eventKey="1" onClick={this.toggleActivityNetworkGraphHeteronym}>
            Heterónimo
          </MenuItem>
          <MenuItem eventKey="1" onClick={this.toggleActivityNetworkGraphDate}>
            Data
          </MenuItem>
          <MenuItem eventKey="1" onClick={this.toggleActivityNetworkGraphTaxonomy}>
            Taxonomia
          </MenuItem>
        </DropdownButton>

      </ButtonToolbar>);
    } else {
      this.activityToRender = this.activityToRender; //(<NetworkGraphContainer pFragmentId={this.props.recommendationArray[this.props.recommendationIndex].interId} pHeteronymWeight="0.0" pTextWeight="1.0" pDateWeight="0.0" ptaxonomyWeight="0.0" onChange={this.props.onChange}/>);
    }

    return <div className="activityMenu">{this.activityToRender}</div>;

  }
}

const ActivityMenu = connect(mapStateToProps, mapDispatchToProps)(ConnectedActivityMenu);

export default ActivityMenu;