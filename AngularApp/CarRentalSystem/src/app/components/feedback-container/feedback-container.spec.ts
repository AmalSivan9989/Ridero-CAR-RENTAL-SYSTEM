import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FeedbackContainer } from './feedback-container';

describe('FeedbackContainer', () => {
  let component: FeedbackContainer;
  let fixture: ComponentFixture<FeedbackContainer>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FeedbackContainer]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FeedbackContainer);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
