/// <reference path="../../../typings/angularjs/angular.d.ts" />
/// <reference path="Product.ts"/>

module hf.marketdataprovider {
    export interface WebAppScope extends ng.IScope {
        products : Product[];
        refreshProducts(): void;
    }
}