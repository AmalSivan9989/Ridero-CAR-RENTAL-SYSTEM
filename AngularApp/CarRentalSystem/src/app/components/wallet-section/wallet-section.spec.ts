import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WalletSection } from './wallet-section';

describe('WalletSection', () => {
  let component: WalletSection;
  let fixture: ComponentFixture<WalletSection>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WalletSection]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WalletSection);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
