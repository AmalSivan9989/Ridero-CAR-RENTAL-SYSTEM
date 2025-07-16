import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AgentCars } from './agent-cars';

describe('AgentCars', () => {
  let component: AgentCars;
  let fixture: ComponentFixture<AgentCars>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AgentCars]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AgentCars);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
