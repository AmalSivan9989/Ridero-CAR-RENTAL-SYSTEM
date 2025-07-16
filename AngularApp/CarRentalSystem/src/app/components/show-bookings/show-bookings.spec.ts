import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowBookings } from './show-bookings';

describe('ShowBookings', () => {
  let component: ShowBookings;
  let fixture: ComponentFixture<ShowBookings>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowBookings]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShowBookings);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
