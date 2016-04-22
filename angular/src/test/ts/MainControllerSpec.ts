/// <reference path="../../../typings/jasmine/jasmine.d.ts" />
/// <reference path="../../../typings/angularjs/angular.d.ts" />
/// <reference path="../../../typings/angularjs/angular-mocks.d.ts" />
/// <reference path="../../main/ts/User.ts" />
/// <reference path="../../main/ts/SimpleUser.ts" />
/// <reference path="../../main/ts/MainController.ts" />

describe('Main Controller', () => {
    var scope: hf.marketdataprovider.WebAppScope;

    beforeEach(() => {
        inject(function ($rootScope: ng.IRootScopeService) {
            scope = <hf.marketdataprovider.WebAppScope>$rootScope.$new();
        })
    });

    it('should have scope with initialized users', () => {
        var controller:hf.marketdataprovider.MainController = new hf.marketdataprovider.MainController(scope);
        expect(controller).toBeDefined();
        expect(scope).toBeDefined();
        expect(scope.users).toBeDefined();
        expect(scope.users.length).toBe(3);
    });
});