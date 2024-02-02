import React, { useState, useRef, useEffect } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import pl from 'date-fns/locale/pl';
import CustomInput from './custom-input.component.js'; 

const setStartOfDay = (date) => {
  return new Date(date.setHours(0, 0, 0, 0));
};

const setEndOfDay = (date) => {
  return new Date(date.setHours(23, 59, 59, 999));
};

const getCurrentDate = () => {
  return new Date();
};

const DatePickerRange = ({ onDatesSelected }) => {
  const [startDate, setStartDate] = useState(null);
  const [endDate, setEndDate] = useState(null);

  useEffect(() => {
    const initialDate = new Date("2018-11-05");
    setStartDate(initialDate);
    onDatesSelected({ startDate: initialDate, endDate });
  }, [onDatesSelected]);

  const handleStartDateChange = (date) => {
    const startOfDay = setStartOfDay(date);
    setStartDate(startOfDay);
    if (endDate && startOfDay > endDate) {
      setEndDate(null);
    }
    onDatesSelected({ startDate: startOfDay, endDate });
  };

  const handleEndDateChange = (date) => {
    const endOfDay = setEndOfDay(date);
    setEndDate(endOfDay);
    if (startDate && endOfDay < startDate) {
      setStartDate(null);
    }
    onDatesSelected({ startDate, endDate: endOfDay });
  };

  const startDateRef = useRef(null);
  const endDateRef = useRef(null);

  return (
    <div className="date-picker-container">
      <h2>Wybierz zakres dat:</h2>
      <DatePicker
        selected={startDate}
        onChange={handleStartDateChange}
        selectsStart
        minDate={new Date("2018-11-05")}
        startDate={startDate}
        endDate={endDate}
        maxDate={getCurrentDate()} 
        placeholderText="Data od"
        customInput={<CustomInput ref={startDateRef} placeholder="Data od"/>}
        locale={pl}
        showYearDropdown
        showMonthDropdown
      />
      <DatePicker
        selected={endDate}
        onChange={handleEndDateChange}
        selectsEnd
        startDate={startDate}
        endDate={endDate}
        minDate={startDate}
        maxDate={getCurrentDate()} 
        placeholderText="Data do"
        customInput={<CustomInput ref={endDateRef} placeholder="Data do"/>}
        locale={pl}
        showYearDropdown
        showMonthDropdown
      />
    </div>
  );
};

export default DatePickerRange;