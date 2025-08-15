import { TestBed } from '@angular/core/testing';

import { LocalStorageService } from './local-storage.service';

describe('LocalStorageService', () => {
  let service: LocalStorageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [LocalStorageService]
    });
    service = TestBed.get(LocalStorageService);
  });

  afterEach(() => localStorage.clear());

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('Teste fixo', () => {
    spyOn(service, 'testLocalStorage');
    service.testLocalStorage();
    expect(service.testLocalStorage).toHaveBeenCalled();
  });

  it('Testando funções de insert, delete e get', () => {
    service.setItem('TEST', 'item');
    expect(service.getItem('TEST')).toBe('item');
    service.removeItem('TEST');
    expect(service.getItem('TEST')).toBe(null);
  });
});