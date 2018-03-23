
import {Inject, Injectable} from "@angular/core";
import {Http, URLSearchParams, Headers, RequestOptions} from "@angular/http";
import {Observable} from "rxjs/Observable";
import {BASE_URL} from "../../app.tokens";
import {Instrument} from "../models/instrument";

@Injectable()
export class MyFinanceDataService{
  constructor(
    private http: Http,
    @Inject(BASE_URL) private baseUrl: string
  ) {
  }

  find(isin: string): Observable<Instrument[]> {
    let url = this.baseUrl + '/filteredinstruments';

    let headers = new Headers()
    headers.set('Accept', 'application/json');

    let search = new URLSearchParams();
    search.set('isin', isin);
    //search.set('to', to);
    console.debug(url);

    let options = new RequestOptions({ headers: headers, search, withCredentials: true });
    return this
      .http
      .get(url, options)
      .map(r => r.json());

  }

  getPositions(): Observable<Position[]> {
    let url = this.baseUrl + '/positions';

    let headers = new Headers()
    headers.set('Accept', 'application/json');

    console.debug(url);

    let options = new RequestOptions({ headers: headers, withCredentials: true });
    return this
      .http
      .get(url, options)
      .map(r => r.json());

  }

}
