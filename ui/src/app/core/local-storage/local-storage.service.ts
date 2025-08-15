import { Injectable } from '@angular/core';

const APP_PREFIX = 'COSTELATA-APP-';

@Injectable()
export class LocalStorageService {
    constructor() { }

    static loadInitialState() {
        return Object.keys(localStorage).reduce((state: any, storegeKey) => {
            if (storegeKey.includes(APP_PREFIX)) {
                const keys = storegeKey
                    .replace(APP_PREFIX, '')
                    .toLowerCase()
                    .split('.')
                    .map(key => key
                        .split('-')
                        .map(
                            (token, index) =>
                                index === 0 ? token : token.charAt(0).toUpperCase() + token.slice(1)
                        )
                        .join('')
                    );

                let currentStateRef = state;
                keys.forEach((key, index) => {
                    if (index === keys.length - 1) {
                        currentStateRef[key] = JSON.parse(<any>localStorage.getItem(storegeKey));
                        return;
                    }

                    currentStateRef[key] = currentStateRef[key] || {};
                    currentStateRef = currentStateRef[key];
                })
            }
            return state;
        })
    }

    setItem(key: string, value: any) {
        localStorage.setItem(`${APP_PREFIX}${key}`, JSON.stringify(value));
    }

    getItem(key: string) {
        let data = localStorage.getItem(`${APP_PREFIX}${key}`);
        return JSON.parse(data && data != "undefined"?data: <any>null);
    }

    removeItem(key: string) {
        localStorage.removeItem(`${APP_PREFIX}${key}`);
    }

    clear() {
        localStorage.clear();
    }

    testLocalStorage() {
        const testValue = 'testValue';
        const testKey = 'testKey';
        let retrievedValue: string;
        const errorMessage = 'Erro';

        this.setItem(testKey, testValue);
        retrievedValue = this.getItem(testKey);
        this.removeItem(testKey);

        if (retrievedValue !== testValue) {
          throw new Error(errorMessage);
        }
      }
}
