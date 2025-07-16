import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FullSummaryReport } from './full-summary-report';

describe('FullSummaryReport', () => {
  let component: FullSummaryReport;
  let fixture: ComponentFixture<FullSummaryReport>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FullSummaryReport]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FullSummaryReport);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
