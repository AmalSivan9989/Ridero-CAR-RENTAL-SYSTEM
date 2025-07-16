import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewAgentRequests } from './view-agent-requests';

describe('ViewAgentRequests', () => {
  let component: ViewAgentRequests;
  let fixture: ComponentFixture<ViewAgentRequests>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ViewAgentRequests]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewAgentRequests);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
