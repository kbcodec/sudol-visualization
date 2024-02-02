import React from 'react';

const CustomInput = React.forwardRef(({ value, onClick, placeholder }, ref) => (
  <input type="text" className="date-picker-input" value={value} onClick={onClick} readOnly ref={ref} placeholder={placeholder} style={{color: '#444', fontSize: '16px'}}/>
));

export default CustomInput;