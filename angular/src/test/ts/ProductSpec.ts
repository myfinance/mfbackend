/// <reference path="../../../typings/jasmine/jasmine.d.ts" />
/// <reference path="../../main/ts/Product.ts" />
/// <reference path="../../main/ts/SimpleProduct.ts" />

describe('Product', () => {
    it('should create product and get attributes', () => {
        var product:hf.marketdataprovider.Product =
            new hf.marketdataprovider.SimpleProduct("123", "Product 1 desc", "1,23");
        expect(product).toBeDefined();
        expect(product.getId()).toBe('123');
        expect(product.getDescription()).toBe('Product 1 desc');
        expect(product.getPrice()).toBe('1,23');
    });
});