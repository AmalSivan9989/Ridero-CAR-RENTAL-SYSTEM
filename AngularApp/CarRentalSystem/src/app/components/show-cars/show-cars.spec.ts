import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowCars } from './show-cars';

describe('ShowCars', () => {
  let component: ShowCars;
  let fixture: ComponentFixture<ShowCars>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowCars]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShowCars);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
