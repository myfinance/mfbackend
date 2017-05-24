import React from 'react';
import ReactDOM from 'react-dom';

import InstrumentSearch from './instrument-search';

const mountNode = document.getElementById('mount');
ReactDOM.render(<InstrumentSearch greeting="Hello"/>, mountNode);