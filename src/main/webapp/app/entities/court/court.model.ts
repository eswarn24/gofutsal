import { BaseEntity } from './../../shared';

export class Court implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public rate?: string,
        public courtLocation?: BaseEntity,
        public courtType?: BaseEntity,
    ) {
    }
}
