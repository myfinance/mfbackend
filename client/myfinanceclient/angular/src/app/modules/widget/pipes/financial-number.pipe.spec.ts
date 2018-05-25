import { FinancialNumberPipe } from './financial-number.pipe';

describe('FinancialNumberPipe', () => {
  it('create an instance', () => {
    const pipe = new FinancialNumberPipe();
    expect(pipe).toBeTruthy();
  });
});
