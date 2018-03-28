import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { CourtLocation } from './court-location.model';
import { CourtLocationService } from './court-location.service';

@Component({
    selector: 'jhi-court-location-detail',
    templateUrl: './court-location-detail.component.html'
})
export class CourtLocationDetailComponent implements OnInit, OnDestroy {

    courtLocation: CourtLocation;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private courtLocationService: CourtLocationService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCourtLocations();
    }

    load(id) {
        this.courtLocationService.find(id)
            .subscribe((courtLocationResponse: HttpResponse<CourtLocation>) => {
                this.courtLocation = courtLocationResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCourtLocations() {
        this.eventSubscriber = this.eventManager.subscribe(
            'courtLocationListModification',
            (response) => this.load(this.courtLocation.id)
        );
    }
}
