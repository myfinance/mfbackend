/**
 * Created by xn01598 on 11.05.2017.
 */
export interface Instrument {
  id: number; // int+double
  isin: string;
  desc: string;
  lastUpdate: string; // ISO-Format: 2017-12-24T17:00:00.000+01:00
}
