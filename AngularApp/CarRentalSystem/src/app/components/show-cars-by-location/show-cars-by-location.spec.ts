import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowCarsByLocation } from './show-cars-by-location';

describe('ShowCarsByLocation', () => {
  let component: ShowCarsByLocation;
  let fixture: ComponentFixture<ShowCarsByLocation>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowCarsByLocation]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShowCarsByLocation);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
