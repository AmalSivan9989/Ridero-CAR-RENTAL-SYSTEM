import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateAvailability } from './update-availability';

describe('UpdateAvailability', () => {
  let component: UpdateAvailability;
  let fixture: ComponentFixture<UpdateAvailability>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdateAvailability]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateAvailability);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
