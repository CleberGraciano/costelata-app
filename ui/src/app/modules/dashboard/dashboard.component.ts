import { Component, OnInit, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderSignalService } from 'src/app/header-signal.service';

@Component({
  standalone: true,
  selector: 'app-dashboard',
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit, AfterViewInit {

  constructor(private headerSignal: HeaderSignalService) { }

  ngOnInit() {}

  ngAfterViewInit() {
    this.headerSignal.title.set('');
    this.headerSignal.breadcrumbs.set([]);
  }

}
