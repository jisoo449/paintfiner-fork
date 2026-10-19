export const filterType = {
  CHECK_BOX_MULTI: 'CHECK_BOX_MULTI',
  CHECK_BOX_SINGLE: 'CHECK_BOX_SINGLE',
  INTEGER: 'INTEGER',
  INTEGER_RANGE: 'INTEGER_RANGE',
  STRING: 'STRING',
}

export const filters = [
  {
    mainFilterName: '품질',
    queryName: 'quality',
    filterType: filterType.CHECK_BOX_MULTI,
    filters: [
      ['중급','Intermediate'],
      ['고급','Highest'],
      ['최고급','Excellent']
    ],
  },
  {
    mainFilterName: '사이즈',
    queryName: 'size',
    filterType: filterType.INTEGER_RANGE,
    filters: [
      'ml',
      'g'
    ]
  },
  {
    mainFilterName: '색상 그룹',
    queryName: 'colorgroup',
    filterType: filterType.CHECK_BOX_MULTI,
    filters: [
      ['노랑','Yellow'],
      ['주황','Orange'],
      ['빨강','Red'],
      ['자주','Violet'],
      ['보라','Purple'],
      ['파랑','Blue'],
      ['초록','Green'],
      ['갈색','Brown'],
      ['하양','White'],
      ['회색','Grey'],
      ['검정','Black']
    ],
  },
  {
    mainFilterName: '질감/농도',
    queryName: 'texture',
    filterType: filterType.CHECK_BOX_MULTI,
    filters:[
      ['헤비바디','Heavy'],
      ['소프트바디','Soft'],
      ['잉크','Ink']
    ]
  },
  {
    mainFilterName: '세트/팩',
    queryName: 'sets',
    filterType: filterType.CHECK_BOX_SINGLE,
    filters: [
      [['세트','Set'],1]
    ]
  },
  // {
  //   mainFilterName: '가격 범위',
  //   queryName: 'price',
  //   filterType: filterType.INTEGER_RANGE,
  //   filters: [
  //     '₩',
  //     '$',
  //     '¥',
  //   ]
  // },
  // {
  //   mainFilterName: '비건',
  //   queryName: 'vegan',
  //   filterType: filterType.CHECK_BOX_SINGLE,
  //   filters:[
  //     [['비건','Vegan'],1],
  //   ]
  // },
  {
    mainFilterName: '투명도',
    queryName: 'transparency',
    filterType: filterType.CHECK_BOX_MULTI,
    filters: [
      ['투명','Transparent'],
      ['반투명','Semi transparent'],
      ['불투명','Opacue'],
    ],
  },
];