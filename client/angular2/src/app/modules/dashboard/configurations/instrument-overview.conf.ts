import { timeParse } from 'd3-time-format';

const d3ParseDateDimension = timeParse('%Y-%m-%d');

export const InstrumentOverviewConf = {
  title: 'Instrument-Overview',
  grid: 'scrollVertical',
  menu: {
    csv: true,
    service: true
  },
  data: {
    columns: [
      { key: 'valdate', type: 'date' },
      { key: 'portfolio', type: 'string' },
      { key: 'typ', type: 'string' },
      { key: 'abteilung', type: 'string' },
      { key: 'gruppe', type: 'string' },
      { key: 'value', type: 'double' },
      { key: 'diff', type: 'double' }
    ]
  },
  dimensions: [
    { id: '81e6ea16-6cba-40a0-8204-0e1f8f103ca5', value: [ 'valdate' ] },
    { id: '5e9c4eb3-8526-42f3-9ea8-091148b2dd92', value: [ 'portfolio' ] },
    { id: '6137e8df-2443-4426-8caf-34a6f51a48f7', value: [ 'portfolio' ] },
    { id: '4f1ba684-1fac-4576-9564-78f8b33c5dee', value: [ 'portfolio' ] },
    { id: 'c83b0f26-c04d-4e4c-8a51-40b727ae4c33', value: [ 'typ' ] },
    { id: 'f0ea4bf9-edfb-450a-9926-6282eeaebab4', value: [ 'typ' ] },
    { id: '41fa84e0-fdc9-4e3c-998d-f5ae0c86cc51', value: [ 'abteilung' ] },
    { id: 'e7329fe8-6a1f-4a6f-b223-70ed4414c42d', value: [ 'portfolio', 'typ' ] },
    { id: 'd42cafb0-3889-44c5-9420-9020174b05ce', value: [ 'gruppe' ] },
    { id: '5304029d-3996-4aff-bafc-11098e3f3d2b', value: [ '' ] }
  ],
  widgets: [
    {
      type: 'horizontal-bar-chart',
      config: {
        title: 'DIFF by Typ',
        dimension: 'c83b0f26-c04d-4e4c-8a51-40b727ae4c33',
        group: dimension => dimension.group().reduceSum(d => d.diff),
        xAxisFormat: 'financial-number',
        tooltip: [
          { key: 'typ', value: 'diff', valueType: 'financial-number' }
        ]
      },
      layout: { x: 2,  y: 0, cols: 2, rows: 2 }
    },
    {
      type: 'composite-line-chart',
      config: {
        title: 'Risk & Limit',
        dimension: '81e6ea16-6cba-40a0-8204-0e1f8f103ca5',
        yAxisFormat: 'financial-number',
        xAxisFormat: 'date',
        lines: [
          {
            title: 'Gesamt',
            group: dimension => dimension.group(d3ParseDateDimension).reduceSum(d => d.value),
            color: 'red',
            tooltip: [
              { key: 'valdate', value: 'value', keyType: 'date', valueType: 'financial-number' }
            ]
          },
          {
            title: 'Marktpreisrisiko',
            group: dimension => dimension.group(d3ParseDateDimension).reduceSum(d => d.gruppe === 'Marktpreisrisiko' ? d.value : 0),
            color: 'blue',
            tooltip: [
              { key: 'valdate', value: 'value', keyType: 'date', valueType: 'financial-number' }
            ]
          },
          {
            title: 'Adressrisiko',
            group: dimension => dimension.group(d3ParseDateDimension).reduceSum(d => d.gruppe === 'Adressrisiko' ? d.value : 0),
            color: 'green',
            tooltip: [
              { key: 'valdate', value: 'value', keyType: 'date', valueType: 'financial-number' }
            ]
          },
          {
            title: 'Sonstiges',
            group: dimension => dimension.group(d3ParseDateDimension).reduceSum(d => d.gruppe === 'Sonstiges' ? d.value : 0),
            color: 'orange',
            tooltip: [
              { key: 'valdate', value: 'value', keyType: 'date', valueType: 'financial-number' }
            ]
          }
        ]
      },
      layout: { x: 0, y: 6, cols: 4, rows: 2 }
    },
  ]
}
