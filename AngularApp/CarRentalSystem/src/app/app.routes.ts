import { Routes } from '@angular/router';

import { AdminLogin } from './components/admin-login/admin-login';
import { CustomerLogin } from './components/customer-login/customer-login';
import { AdminMenu } from './components/admin-menu/admin-menu';
import { CustomerMenu } from './components/customer-menu/customer-menu';
import { ShowUsers } from './components/show-users/show-users';
import { ShowBookings } from './components/show-bookings/show-bookings';
import { ShowCars } from './components/show-cars/show-cars';
import { AddCar } from './components/add-car/add-car';
import { UpdateAvailability } from './components/update-availability/update-availability';
import { MainMenu } from './components/main-menu/main-menu';
import { ShowCarsByLocation } from './components/show-cars-by-location/show-cars-by-location';
import { PendingPayments } from './components/pending-payments/pending-payments';
import { CreateBooking } from './components/create-booking/create-booking';
import { PaymentHistory } from './components/payment-history/payment-history';
import { CarDelete } from './components/car-delete/car-delete';
import { Register } from './components/register/register';
import { ManageUsers } from './components/manage-users/manage-users';
import { ReportIssue } from './components/report-issue/report-issue';
import { ViewAgentRequests } from './components/view-agent-requests/view-agent-requests';
import { FullSummaryReport } from './components/full-summary-report/full-summary-report';
import { AuthGuard } from './auth-guard';
import { MyBookings } from './components/my-bookings/my-bookings';
import { WalletSection } from './components/wallet-section/wallet-section';
import { Cars } from './components/cars/cars';
import { FeedbackContainer } from './components/feedback-container/feedback-container';
import { CarFeedback } from './components/car-feedback/car-feedback';
import { AgentLogin } from './components/agent-login/agent-login';
import { AgentMenu } from './components/agent-menu/agent-menu';

import { AgentCars } from './components/agent-cars/agent-cars';
import { MaintenanceRequests } from './components/maintenance-requests/maintenance-requests';
import { AgentRequests } from './components/agent-requests/agent-requests';

export const routes: Routes = [
  { path: '', component: MainMenu },
  { path: 'adminLogin', component: AdminLogin },
  { path: 'customerLogin', component: CustomerLogin },
  { path: 'register', component: Register },
  {path:'agentLogin',component:AgentLogin},

  {
    path: 'adminMenu',
    component: AdminMenu,
    canActivate: [AuthGuard],
    children: [
      { path: 'manageUsers', component: ManageUsers },
      {path:'cars',component:Cars},
      { path: 'addCar', component: AddCar },
      { path: 'showUsers', component: ShowUsers },
      { path: 'showBookings', component: ShowBookings },
      { path: 'showCars', component: ShowCars },
      { path: 'updateCarAvailability', component: UpdateAvailability },
      { path:'feedbackContainer',component:FeedbackContainer},
      { path: 'carFeedback/:id', component: CarFeedback },
      { path: 'deleteCar', component: CarDelete },
      { path: 'reportIssue', component: ReportIssue },
      { path: 'viewAgentRequests', component: ViewAgentRequests },
      { path: 'fullSummaryReport', component: FullSummaryReport }
    ]
  },

  {
    path: 'customerMenu',
    component: CustomerMenu,
    canActivate: [AuthGuard],
   
    children: [
      { path: 'showCarsByLocation', component: ShowCarsByLocation },
      { path: 'createBooking', component: CreateBooking },
      { path: 'pendingPayments', component: PendingPayments },
      { path: 'paymentHistory', component: PaymentHistory },
      { path: 'myBookings', component: MyBookings },
      { path: 'wallet', component: WalletSection }
    ]
  },
{
    path: 'agentMenu',
    component: AgentMenu,
    canActivate: [AuthGuard],
    children: [
      {path:'agentMenu/agentCars',component:AgentCars,},
       { path: 'agentMenu/AgentRequests', component: AgentRequests },
     { path: 'agentMenu/maintenanceRequests', component: MaintenanceRequests }
    ]
  },
 
  {
    path: 'feedback/:carId',
    loadComponent: () =>
      import('./components/feedback-form/feedback-form').then(m => m.FeedbackForm)
  },

  { path: '**', redirectTo: '' }
];
