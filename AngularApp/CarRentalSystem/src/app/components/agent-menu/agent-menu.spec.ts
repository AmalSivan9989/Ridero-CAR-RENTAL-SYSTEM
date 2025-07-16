import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AgentMenu } from './agent-menu';

describe('AgentMenu', () => {
  let component: AgentMenu;
  let fixture: ComponentFixture<AgentMenu>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AgentMenu]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AgentMenu);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
