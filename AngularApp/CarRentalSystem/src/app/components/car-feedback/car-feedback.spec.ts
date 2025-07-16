import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CarFeedback } from './car-feedback';

describe('CarFeedback', () => {
  let component: CarFeedback;
  let fixture: ComponentFixture<CarFeedback>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CarFeedback]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CarFeedback);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
