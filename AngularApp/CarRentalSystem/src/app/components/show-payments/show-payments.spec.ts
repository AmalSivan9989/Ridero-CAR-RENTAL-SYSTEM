import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowPayments } from './show-payments';

describe('ShowPayments', () => {
  let component: ShowPayments;
  let fixture: ComponentFixture<ShowPayments>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowPayments]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShowPayments);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
