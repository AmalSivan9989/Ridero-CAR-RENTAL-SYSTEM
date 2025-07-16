import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CarDelete } from './car-delete';

describe('CarDelete', () => {
  let component: CarDelete;
  let fixture: ComponentFixture<CarDelete>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CarDelete]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CarDelete);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
