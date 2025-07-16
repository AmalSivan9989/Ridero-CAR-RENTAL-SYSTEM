import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AgentViewRequests } from './agent-view-requests';

describe('AgentViewRequests', () => {
  let component: AgentViewRequests;
  let fixture: ComponentFixture<AgentViewRequests>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AgentViewRequests]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AgentViewRequests);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
