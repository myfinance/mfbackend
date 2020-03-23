import { Injectable, Injector } from '@angular/core';
import { HttpInterceptor as BaseHttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import {ConfigService} from "./shared/services/config.service";

//import { ConfigService } from './services/config.service';
//import { UserService } from './services/user.service';

@Injectable()
export class HttpInterceptor implements BaseHttpInterceptor {

  constructor (private _injector: Injector) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    //if(req.url.indexOf('assets/config.json') !== -1) {
    //  return next.handle(req);
    //}

    // Workaround to prevent a cyclic dependency.
    //let configService = this._injector.get(ConfigService);
    //let currentZone = configService.get('currentZone');


    //if(req.url.indexOf('http://') === -1 && req.url.indexOf('https://') === -1) {
    //  return next.handle(req.clone({ url: currentZone.url + req.url.replace("###", ""), withCredentials: true}));
    //}

    return next.handle(req.clone({ url: req.url, withCredentials: true}));
  }

}
