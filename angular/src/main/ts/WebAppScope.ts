/// <reference path="../../../typings/angularjs/angular.d.ts" />
/// <reference path="User.ts"/>

module hf.marketdataprovider {
    export interface WebAppScope extends ng.IScope {
        users : User[];
    }
}