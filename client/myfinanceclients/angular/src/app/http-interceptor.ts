import { Injectable, Injector } from '@angular/core';
import { HttpInterceptor as BaseHttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

//import { ConfigService } from './services/config.service';
//import { UserService } from './services/user.service';

@Injectable()
export class HttpInterceptor implements BaseHttpInterceptor {

  constructor (private _injector: Injector) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Exclude configuration load from the interceptor.
    // TODO: Probably wait for angular to allow interceptors not solely on global level.
    //if(req.url.indexOf('assets/config.json') !== -1) {
    //  return next.handle(req);
    //}

    // Workaround to prevent a cyclic dependency.
    //let configService = this._injector.get(ConfigService);
    //let currentZone = configService.get('currentZone');

    //let userService = this._injector.get(UserService);

    /*if(req.url.indexOf('http://') === -1 && req.url.indexOf('https://') === -1) {
      return next.handle(req.clone({ url: currentZone.url + req.url, withCredentials: true}));//, headers: req.headers.set('Authorization', userService.getAuthorization()) }));
    }*/

    return next.handle(req.clone({url: req.url, withCredentials: true}));
  }

}
