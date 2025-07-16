import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AgentRequests } from './agent-requests';

describe('AgentRequests', () => {
  let component: AgentRequests;
  let fixture: ComponentFixture<AgentRequests>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AgentRequests]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AgentRequests);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
