import {ADD_FRAGMENT} from "../constants/action-types";
import {SET_FRAGMENT_INDEX} from "../constants/action-types";
import {SET_CURRENT_VISUALIZATION} from "../constants/action-types";
import {ADD_HISTORY_ENTRY} from "../constants/action-types";
import {SET_ALL_FRAGMENTS_LOADED} from "../constants/action-types";
import {SET_OUT_OF_LANDING_PAGE} from "../constants/action-types";
import {SET_HISTORY_ENTRY_COUNTER} from "../constants/action-types";
import {SET_RECOMMENDATION_ARRAY} from "../constants/action-types";
import {SET_RECOMMENDATION_INDEX} from "../constants/action-types";
import {SET_FRAGMENTS_HASHMAP} from "../constants/action-types";
import {SET_CURRENT_FRAGMENT_MODE} from "../constants/action-types";
import {SET_RECOMMENDATION_LOADED} from "../constants/action-types";
import {SET_VISUALIZATION_TECHNIQUE} from "../constants/action-types";
import {SET_SEMANTIC_CRITERIA} from "../constants/action-types";
import {SET_SEMANTIC_CRITERIA_DATA} from "../constants/action-types";
import {SET_SEMANTIC_CRITERIA_DATA_LOADED} from "../constants/action-types";
import {SET_POTENTIAL_VISUALIZATION_TECHNIQUE} from "../constants/action-types";
import {SET_POTENTIAL_SEMANTIC_CRITERIA} from "../constants/action-types";
import {SET_POTENTIAL_SEMANTIC_CRITERIA_DATA} from "../constants/action-types";

const initialState = {
  fragments: [],
  fragmentIndex: 0,
  currentVisualization: "Configure primeiro uma actividade para poder ter uma vista global!",
  history: [],
  allFragmentsLoaded: false,
  outOfLandingPage: false,
  historyEntryCounter: 0,
  recommendationArray: [],
  recommendationIndex: 0,
  fragmentsHashMap: [],
  currentFragmentMode: true,
  recommendationLoaded: true,
  visualizationTechnique: 0,
  semanticCriteria: 0,
  semanticCriteriaData: [],
  semanticCriteriaDataLoaded: true,
  potentialVisualizationTechnique: 0,
  potentialSemanticCriteria: 0,
  potentialSemanticCriteriaData: []
};

const rootReducer = (state = initialState, action) => {
  switch (action.type) {
    case ADD_FRAGMENT:
      return {
        ...state,
        fragments: [
          ...state.fragments,
          action.payload
        ]
      };
    case SET_FRAGMENT_INDEX:
      return {
        ...state,
        fragmentIndex: action.payload
      };
    case SET_CURRENT_VISUALIZATION:
      return {
        ...state,
        currentVisualization: action.payload
      };
    case ADD_HISTORY_ENTRY:
      return {
        ...state,
        history: [
          ...state.history,
          action.payload
        ]
      };
    case SET_ALL_FRAGMENTS_LOADED:
      return {
        ...state,
        allFragmentsLoaded: action.payload
      };
    case SET_OUT_OF_LANDING_PAGE:
      return {
        ...state,
        outOfLandingPage: action.payload
      };
    case SET_HISTORY_ENTRY_COUNTER:
      return {
        ...state,
        historyEntryCounter: action.payload
      };
    case SET_RECOMMENDATION_ARRAY:
      return {
        ...state,
        recommendationArray: action.payload
      };
    case SET_RECOMMENDATION_INDEX:
      return {
        ...state,
        recommendationIndex: action.payload
      };
    case SET_FRAGMENTS_HASHMAP:
      return {
        ...state,
        fragmentsHashMap: action.payload
      };
    case SET_CURRENT_FRAGMENT_MODE:
      return {
        ...state,
        currentFragmentMode: action.payload
      };
    case SET_RECOMMENDATION_LOADED:
      return {
        ...state,
        recommendationLoaded: action.payload
      };
    case SET_VISUALIZATION_TECHNIQUE:
      return {
        ...state,
        visualizationTechnique: action.payload
      };
    case SET_SEMANTIC_CRITERIA:
      return {
        ...state,
        semanticCriteria: action.payload
      };
    case SET_SEMANTIC_CRITERIA_DATA:
      return {
        ...state,
        semanticCriteriaData: action.payload
      };
    case SET_SEMANTIC_CRITERIA_DATA_LOADED:
      return {
        ...state,
        semanticCriteriaDataLoaded: action.payload
      };
    case SET_POTENTIAL_VISUALIZATION_TECHNIQUE:
      return {
        ...state,
        potentialVisualizationTechnique: action.payload
      };
    case SET_POTENTIAL_SEMANTIC_CRITERIA:
      return {
        ...state,
        potentialSemanticCriteria: action.payload
      };
    case SET_POTENTIAL_SEMANTIC_CRITERIA_DATA:
      return {
        ...state,
        potentialSemanticCriteriaData: action.payload
      };
    default:
      return state;
  }
};

export default rootReducer;