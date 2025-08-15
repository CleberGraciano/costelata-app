import { Component, OnInit, inject, computed } from '@angular/core';
import { HeaderButton, HeaderSignalService } from 'src/app/header-signal.service';

@Component({
  selector: 'app-cabecalho-breadcrumb',
  templateUrl: './cabecalho-breadcrumb.component.html',
  styleUrls: ['./cabecalho-breadcrumb.component.css']
})
export class CabecalhoBreadcrumbComponent implements OnInit {
  private headerSignal = inject(HeaderSignalService);

  title = computed(() => this.headerSignal.title());
  actionPage = computed(() => this.headerSignal.actionPage());
  breadcrumbs = computed(() => this.headerSignal.breadcrumbs());
  actionButton = computed<HeaderButton | null>(() => this.headerSignal.actionButton());

  ngOnInit(): void {}
}
