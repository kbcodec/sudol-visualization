import React from 'react';
import { css } from '@emotion/react';
import { BeatLoader } from 'react-spinners';

const override = css`
  display: block;
  margin: 0 auto;
  border-color: red;
`;

const Spinner = () => {
  return (
    <div className="spinner-container">
      <BeatLoader css={override} size={15} color={'#00BFFF'} loading={true} />
    </div>
  );
};

export default Spinner;