/**
 * Created by xn01598 on 11.05.2017.
 */
export interface Instrument {
  instrumentid: number; // int+double
  isin: string;
  description: string;
  treelastchanged: string; // ISO-Format: 2017-12-24T17:00:00.000+01:00
}
