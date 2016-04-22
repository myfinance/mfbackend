module hf.marketdataprovider {
    export interface User {
        getId(): string;
        getEmail(): string;
        getFullName(): string;
    }
}