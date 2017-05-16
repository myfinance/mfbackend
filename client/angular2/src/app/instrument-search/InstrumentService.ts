import {Inject, Injectable} from "@angular/core";
import {Http, URLSearchParams, Headers, RequestOptions} from "@angular/http";
import { BASE_URL } from "../app.tokens";
import {Observable} from "rxjs";
import {Instrument} from "../entities/instrument";
import 'rxjs/add/operator/map';
/**
 * Created by xn01598 on 11.05.2017.
 */

@Injectable()
export class InstrumentService{
  constructor(
    private http: Http,
    @Inject(BASE_URL) private baseUrl: string
  ) {
    console.debug('Viele Grüße aus dem Ctor!');
  }

  find(): Observable<Instrument[]> {
    let url= 'http://karaf:karaf@localhost:8181/dac/rest/marketdata/environments/dev/instruments';
    //let url = this.baseUrl + '/instruments';

    let headers = new Headers()
    headers.set('Accept', 'application/json');

    let search = new URLSearchParams();
    //search.set('from', from);
    //search.set('to', to);
    console.debug(url);
    /*var jwt = localStorage.getItem('id_token');

    if(jwt) {
      headers.append('Authorization', 'Bearer ' + jwt);
    }*/
    var creds = "karaf=karaf&password=karaf";
    let options = new RequestOptions({ headers: headers, withCredentials: true });
    return this
      .http
      .get(url, options)
      .map(r => r.json());
    /*return this
      .http
      .get(url, { headers })
      .map(r => r.json());*/

  }

}
