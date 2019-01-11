import {ADD_FRAGMENT} from "../constants/action-types";
import {SET_FRAGMENT_INDEX} from "../constants/action-types";
import {SET_CURRENT_VISUALIZATION} from "../constants/action-types";
import {ADD_HISTORY_ENTRY} from "../constants/action-types";
import {SET_ALL_FRAGMENTS_LOADED} from "../constants/action-types";
import {SET_OUT_OF_LANDING_PAGE} from "../constants/action-types";
import {SET_HISTORY_ENTRY_COUNTER} from "../constants/action-types"
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

export const addFragment = fragment => ({type: ADD_FRAGMENT, payload: fragment});

export const setFragmentIndex = fragmentIndex => ({type: SET_FRAGMENT_INDEX, payload: fragmentIndex});

export const setCurrentVisualization = currentVisualization => ({type: SET_CURRENT_VISUALIZATION, payload: currentVisualization});

export const addHistoryEntry = historyEntry => ({type: ADD_HISTORY_ENTRY, payload: historyEntry});

export const setAllFragmentsLoaded = allFragmentsLoaded => ({type: SET_ALL_FRAGMENTS_LOADED, payload: allFragmentsLoaded});

export const setOutOfLandingPage = outOfLandingPage => ({type: SET_OUT_OF_LANDING_PAGE, payload: outOfLandingPage});

export const setHistoryEntryCounter = historyEntryCounter => ({type: SET_HISTORY_ENTRY_COUNTER, payload: historyEntryCounter});

export const setRecommendationArray = recommendationArray => ({type: SET_RECOMMENDATION_ARRAY, payload: recommendationArray});

export const setRecommendationIndex = recommendationIndex => ({type: SET_RECOMMENDATION_INDEX, payload: recommendationIndex});

export const setfragmentsHashMap = fragmentsHashMap => ({type: SET_FRAGMENTS_HASHMAP, payload: fragmentsHashMap});

export const setCurrentFragmentMode = currentFragmentMode => ({type: SET_CURRENT_FRAGMENT_MODE, payload: currentFragmentMode});

export const setRecommendationLoaded = recommendationLoaded => ({type: SET_RECOMMENDATION_LOADED, payload: recommendationLoaded});

export const setVisualizationTechnique = visualizationTechnique => ({type: SET_VISUALIZATION_TECHNIQUE, payload: visualizationTechnique});

export const setSemanticCriteria = semanticCriteria => ({type: SET_SEMANTIC_CRITERIA, payload: semanticCriteria});

export const setSemanticCriteriaData = semanticCriteriaData => ({type: SET_SEMANTIC_CRITERIA_DATA, payload: semanticCriteriaData});

export const setSemanticCriteriaDataLoaded = semanticCriteriaDataLoaded => ({type: SET_SEMANTIC_CRITERIA_DATA_LOADED, payload: semanticCriteriaDataLoaded});

export const setPotentialVisualizationTechnique = potentialVisualizationTechnique => ({type: SET_POTENTIAL_VISUALIZATION_TECHNIQUE, payload: potentialVisualizationTechnique});

export const setPotentialSemanticCriteria = potentialSemanticCriteria => ({type: SET_POTENTIAL_SEMANTIC_CRITERIA, payload: potentialSemanticCriteria});

export const setPotentialSemanticCriteriaData = potentialSemanticCriteriaData => ({type: SET_POTENTIAL_SEMANTIC_CRITERIA_DATA, payload: potentialSemanticCriteriaData});