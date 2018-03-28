import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Court } from './court.model';
import { CourtService } from './court.service';

@Component({
    selector: 'jhi-court-detail',
    templateUrl: './court-detail.component.html'
})
export class CourtDetailComponent implements OnInit, OnDestroy {

    court: Court;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private courtService: CourtService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCourts();
    }

    load(id) {
        this.courtService.find(id)
            .subscribe((courtResponse: HttpResponse<Court>) => {
                this.court = courtResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCourts() {
        this.eventSubscriber = this.eventManager.subscribe(
            'courtListModification',
            (response) => this.load(this.court.id)
        );
    }
}
